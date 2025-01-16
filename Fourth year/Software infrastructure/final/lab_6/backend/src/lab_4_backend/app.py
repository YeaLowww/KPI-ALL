import os
from flask import Flask, jsonify, request, Response, make_response
from functools import wraps
from marshmallow import Schema, fields
import connectors
from typing import Any, ParamSpec, Callable, TypeAlias, TypeVar, cast, Optional
from http import HTTPStatus
from mysql.connector.cursor import MySQLCursor
from mysql.connector import MySQLConnection
import logging
import sys
import io
import pika
import imghdr

logger = logging.getLogger('backendLogger')
logger.addHandler(logging.StreamHandler(sys.stdout))
logger.setLevel(logging.DEBUG)
IMAGE_DIR = '/backend/images'

app = Flask(__name__)

T = TypeVar('T')

class StudentIdSchema(Schema):
    studentId: fields.Integer = fields.Integer(required=True)

class GroupChangeSchema(Schema):
    studentId: fields.Integer = fields.Integer(required=True)
    groupId: fields.Integer = fields.Integer(required=True)

class StudentAddSchema(Schema):
    groupId: fields.Integer = fields.Integer(required=True)
    name: fields.String = fields.String(required=True)
    surname: fields.String = fields.String(required=True)
    image: fields.Raw = fields.Raw(required=False)

STUDENT_ID_SCHEMA = StudentIdSchema()
GROUP_CHANGE_SCHEMA = GroupChangeSchema()
STUDENTS_ADD_SCHEMA = StudentAddSchema()

def cast_to_schema(schema: Schema, json_data: Any) -> Any:
    return cast(Any, schema.load(json_data))

P = ParamSpec('P')
R = TypeVar('R')
JsonDict: TypeAlias = dict[str, Any]

def db_operation(func: Callable[..., T]) -> Callable[..., T]:
    @wraps(func)
    def wrapper(*args: Any, **kwargs: Any) -> T:
        db: Optional[MySQLConnection] = None
        try:
            db = cast(MySQLConnection, connectors.mysql_conn())
            cur: MySQLCursor = db.cursor()
            result: T = func(cur, *args, **kwargs)
            db.commit()
            return result
        except Exception as e:
            if db:
                db.rollback()
            raise e
        finally:
            if db:
                db.close()
    return wrapper

def send_message(message: str):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='rabbitmq'))
    channel = connection.channel()
    channel.queue_declare(queue='student_updates')  # Створюємо чергу, якщо вона не існує
    channel.basic_publish(exchange='', routing_key='student_updates', body=message)
    connection.close()

Student = tuple[int, int, str, str]  # id, groupId, name, surname
Group = tuple[int, str]  # id, name

@app.route("/students/get/", methods=["GET"])

# images from database

# def students_get() -> Response:
#     @db_operation
#     def get_all_students(cursor: MySQLCursor) -> list[Student]:
#         cursor.execute("""
#           SELECT s.id, s.name, s.surname, s.groupId,
#                  CASE WHEN i.image IS NOT NULL THEN 1 ELSE 0 END AS has_image
#           FROM students s
#           LEFT JOIN student_images i ON s.id = i.student_id
#         """)
#         return cast(list[Student], cursor.fetchall())
#     try:
#         result: list[Student] = get_all_students()
#         return make_response(jsonify({"data": result}), HTTPStatus.OK)
#     except Exception as e:
#         return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)


# images from docker volume
def students_get() -> Response:
    @db_operation
    def get_all_students(cursor: MySQLCursor) -> list[Student]:
        cursor.execute("""
          SELECT s.id, s.name, s.surname, s.groupId
          FROM students s
        """)
        return cast(list[Student], cursor.fetchall())
    
    try:
        result: list[Student] = get_all_students()
        updated_result = []
        for student in result:
            has_image = int(os.path.exists(os.path.join(IMAGE_DIR, f"{student[0]}.jpg")))
            updated_student = student + (has_image,)
            updated_result.append(updated_student)
        return make_response(jsonify({"data": updated_result}), HTTPStatus.OK)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

#####

@app.route("/students/getById/", methods=["GET"])
def students_get_by_id() -> Response:
    @db_operation
    def get_student(cursor: MySQLCursor) -> Optional[Student]:
        data = cast_to_schema(STUDENT_ID_SCHEMA, request.get_json())
        cursor.execute("SELECT * FROM students WHERE id = %s", (data['studentId'],))
        return cast(Optional[Student], cursor.fetchone())
    try:
        result: Optional[Student] = get_student()
        if not result:
            return make_response(jsonify({"error": "Student not found"}), HTTPStatus.NOT_FOUND)
        return make_response(jsonify({"data": result}), HTTPStatus.OK)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

@app.route("/students/change/add/", methods=["POST"])
def students_add() -> Response:
    @db_operation
    def add_student(cursor: MySQLCursor) -> None:
        data = cast_to_schema(STUDENTS_ADD_SCHEMA, request.get_json())
        cursor.execute(
            "INSERT INTO students (groupId, name, surname) VALUES (%s, %s, %s)",
            (data['groupId'], data['name'], data['surname'])
        )
        student_id = cursor.lastrowid

        if 'image' in data:
            cursor.execute(
                "INSERT INTO student_images (student_id, image) VALUES (%s , %s)",
                (student_id, data['image'])
            )
        send_message(f"Student added: {data['name']} {data['surname']} (ID: {student_id})")  

    try:
        add_student()
        return make_response(jsonify({"success": True}), HTTPStatus.CREATED)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

@app.route("/students/change/delete/", methods=["POST"])
def students_delete() -> Response:
    @db_operation
    def delete_student(cursor: MySQLCursor) -> None:
        data = cast_to_schema(STUDENT_ID_SCHEMA, request.get_json())
        cursor.execute("DELETE FROM students WHERE id = %s", (data['studentId'],))
        send_message(f"Student deleted: ID {data['studentId']}") 

    try:
        delete_student()
        return make_response(jsonify({"success": True}), HTTPStatus.OK)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

@app.route("/students/change/group/", methods=["PUT"])
def students_change_group() -> Response:
    @db_operation
    def change_group(cursor: MySQLCursor) -> None:
        data = cast_to_schema(GROUP_CHANGE_SCHEMA, request.get_json())
        logger.debug(f'change_group: {data}')
        cursor.execute(
            "UPDATE students SET groupId = %s WHERE id = %s",
            (data['groupId'], data['studentId'])
        )
        send_message(f"Student group changed: ID {data['studentId']} to Group ID {data['groupId']}")  # Відправка повідомлення до RabbitMQ

    try:
        change_group()
        return make_response(jsonify({"success": True}), HTTPStatus.OK)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

@app.route("/groups/get/", methods=["GET"])
def groups_get() -> Response:
    @db_operation
    def get_all_groups(cursor: MySQLCursor) -> list[Group]:
        cursor.execute("SELECT * FROM student_groups")
        return cast(list[Group], cursor.fetchall())
    
    try:
        result: list[Group] = get_all_groups()
        return make_response(jsonify({"data": result}), HTTPStatus.OK)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

@app.route("/schedule/get/", methods=["GET"])
def schedule_get() -> Response:
    try:
        db = connectors.mongo_conn()['schedules_db']
        coll = db["schedule_collection"]
        result = coll.find()
        result_list: list[JsonDict] = [dict(doc, _id=str(doc['_id'])) for doc in result]
        logger.debug(f'schedule_get: {result_list}')
        return make_response(jsonify({"data": result_list}), HTTPStatus.OK)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

# images in database

# @app.route("/students/<int:student_id>/upload_image/", methods=["POST"])
# def upload_student_image(student_id: int) -> Response:
#     if "file" not in request.files:
#         return make_response(jsonify({"error": "No file part"}), HTTPStatus.BAD_REQUEST)
#     file = request.files["file"]
#     if file.filename == "":
#         return make_response(jsonify({"error": "No selected file"}), HTTPStatus.BAD_REQUEST)

#     # Read and validate
#     contents = file.read()
#     file.seek(0)
#     file_type = imghdr.what(None, contents)
#     if file_type not in ("jpeg", "png", "gif"):
#         return make_response(jsonify({"error": "Invalid file format"}), HTTPStatus.BAD_REQUEST)

#     @db_operation
#     def upsert_image(cursor: MySQLCursor) -> None:
#         cursor.execute("SELECT image FROM student_images WHERE student_id = %s", (student_id,))
#         existing = cursor.fetchone()
#         if existing:
#             cursor.execute(
#                 "UPDATE student_images SET image = %s WHERE student_id = %s", 
#                 (contents, student_id)
#             )
#         else:
#             cursor.execute(
#                 "INSERT INTO student_images (student_id, image) VALUES (%s, %s)",
#                 (student_id, contents)
#             )

#     try:
#         upsert_image()
#         send_message(f"Image added for student ID {student_id}")
#         return make_response(jsonify({"success": True}), HTTPStatus.CREATED)
#     except Exception as e:
#         return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)



# @app.route("/students/<int:student_id>/get_image/", methods=["GET"])
# def get_student_image(student_id: int) -> Response:
#     @db_operation
#     def fetch_image(cursor: MySQLCursor) -> Optional[bytes]:
#         cursor.execute("SELECT image FROM student_images WHERE student_id = %s LIMIT 1", (student_id,))
#         row = cursor.fetchone()
#         return row[0] if row else None

#     try:
#         image_data = fetch_image()
#         if not image_data:
#             return make_response(jsonify({"error": "Image not found"}), HTTPStatus.NOT_FOUND)

#         # Infer mime type from binary content
#         mime_type = imghdr.what(None, image_data)
#         if mime_type == "jpeg":
#             mime_type = "jpg"
#         return Response(io.BytesIO(image_data), mimetype=f"image/{mime_type}")
#     except Exception as e:
#         return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)



# @app.route("/students/<int:student_id>/delete_image/", methods=["DELETE"])
# def delete_student_image(student_id: int) -> Response:
#     @db_operation
#     def remove_image(cursor: MySQLCursor) -> None:
#         cursor.execute("DELETE FROM student_images WHERE student_id = %s", (student_id,))
#         send_message(f"Image deleted for student ID {student_id}")

#     try:
#         remove_image()
#         return make_response(jsonify({"success": True}), HTTPStatus.OK)
#     except Exception as e:
#         return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)





# images from docker volume

os.makedirs(IMAGE_DIR, exist_ok=True)

def save_image(file, student_id):
    try:
        file_path = os.path.join(IMAGE_DIR, f"{student_id}.jpg")
        file.save(file_path)
        return file_path
    except Exception as e:
        raise Exception(f"Failed to save image: {str(e)}")

def remove_image(student_id):
    try:
        file_path = os.path.join(IMAGE_DIR, f"{student_id}.jpg")
        if os.path.exists(file_path):
            os.remove(file_path)
            return True
        else:
            raise Exception("Image not found")
    except Exception as e:
        raise Exception(f"Failed to remove image: {str(e)}")


@app.route("/students/<int:student_id>/upload_image/", methods=["POST"])
def upload_student_image(student_id: int) -> Response:
    if "file" not in request.files:
        return make_response(jsonify({"error": "No file part"}), HTTPStatus.BAD_REQUEST)
    file = request.files["file"]
    if file.filename == "":
        return make_response(jsonify({"error": "No selected file"}), HTTPStatus.BAD_REQUEST)

    # Read and validate
    contents = file.read()
    file.seek(0)
    file_type = imghdr.what(None, contents)
    if file_type not in ("jpeg", "png", "gif"):
        return make_response(jsonify({"error": "Invalid file format"}), HTTPStatus.BAD_REQUEST)

    try:
        file_path = save_image(file, student_id)
        send_message(f"Image uploaded for student ID {student_id}")
        return make_response(jsonify({"success": True, "file_path": file_path}), HTTPStatus.CREATED)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)


@app.route("/students/<int:student_id>/get_image/", methods=["GET"])
def get_student_image(student_id: int) -> Response:
    try:
        file_path = os.path.join(IMAGE_DIR, f"{student_id}.jpg")
        if not os.path.exists(file_path):
            return make_response(jsonify({"error": "Image not found"}), HTTPStatus.NOT_FOUND)
        
        with open(file_path, 'rb') as img_file:
            image_data = img_file.read()
        
        mime_type = imghdr.what(None, image_data)
        if mime_type == "jpeg":
            mime_type = "jpg"
        return Response(io.BytesIO(image_data), mimetype=f"image/{mime_type}")
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)


@app.route("/students/<int:student_id>/delete_image/", methods=["DELETE"])
def delete_student_image(student_id: int) -> Response:
    try:
        file_path = os.path.join(IMAGE_DIR, f"{student_id}.jpg")
        if os.path.exists(file_path):
            os.remove(file_path)
            send_message(f"Image deleted for student ID {student_id}")
            return make_response(jsonify({"success": True}), HTTPStatus.OK)
        else:
            return make_response(jsonify({"error": "Image not found"}), HTTPStatus.NOT_FOUND)
    except Exception as e:
        return make_response(jsonify({"error": str(e)}), HTTPStatus.INTERNAL_SERVER_ERROR)

####

if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=55002)