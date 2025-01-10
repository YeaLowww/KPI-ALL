from flask import Flask, jsonify, request
import connectors

app = Flask(__name__)
DEBUG = True


""" STUDENTS """

@app.route("/students/get", methods=["POST"])
def studentsGet():
    db = connectors.mysql_conn()
    cur = db.cursor()
    
    cur.execute("SELECT * FROM students")
    result = cur.fetchall()
    
    db.close()
    return jsonify({"data": result}, 200)



@app.route("/students/getById", methods=["POST"])
def studentsGetById():
    """
        Expected request: {
            "studentId": int
        }
    """
    db = connectors.mysql_conn()
    cur = db.cursor()
    
    id = request.json["studentId"]
    cur.execute(f"SELECT * FROM students WHERE id = {id}")
    result = cur.fetchone()
    
    db.close()
    return jsonify({"data": result}, 200)

@app.route("/students/change/add", methods=["POST"])
def studentsAdd():
    """
        Expected request: {
            "groupId": int,
            "name": str,
            "surname": str
        }
    """
    db = connectors.mysql_conn()
    cur = db.cursor()
    
    data = request.json
    cur.execute("INSERT INTO students (groupId, name, surname) "+
                f"VALUES ({data['groupId']}, '{data['name']}', '{data['surname']}')")
    db.commit()
    
    db.close()
    return jsonify(success=True)

@app.route("/students/change/delete", methods=["POST"])
def studentsDelete():
    """
        Expected request: {
            "studentId": int,
        }
    """
    db = connectors.mysql_conn()
    cur = db.cursor()
    
    data = request.json
    cur.execute(f"DELETE FROM students WHERE id = {data['studentId']}")
    db.commit()
    
    db.close()
    return jsonify(success=True)


""" GRUOPS """

@app.route("/groups/get", methods=["POST"])
def groupsGet():
    db = connectors.mysql_conn()
    cur = db.cursor()
    
    cur.execute("SELECT * FROM groups")
    result = cur.fetchall()
    
    db.close()
    return jsonify({"data": result}, 200)

# blob image
@app.route("/groups/change/image", methods=["POST"])
def changeImage():
    db = connectors.mysql_conn()
    cur = db.cursor()
    
    
    cur.execute("UPDATE groups "+
                f"SET image={request.files.get('image')}, mime_type={request.json['mimetype']} "+
                f"WHERE id={request.json['groupId']}")
    db.commit()
    
    db.close()
    return jsonify(success=True)


""" SCHEDULE """

@app.route("/schedule/get", methods=["POST"])
def scheduleGet():
    db = connectors.mongo_conn()['schedules_db']
    coll = db["schedule_collection"]
    
    result = coll.find()
    
    return jsonify({"data": result}, 200)

if __name__ == "__main__":
     app.run(host='0.0.0.0', port=5001) 