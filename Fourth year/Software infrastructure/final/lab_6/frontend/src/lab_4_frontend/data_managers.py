import requests
from typing import Any, Optional
import json
import logging
import sys
import os

GROUPS_ADDRESS = "http://backend:55002/groups/"
STUDENTS_ADDRESS = "http://backend:55002/students/"
SCHEDULE_ADDRESS = "http://backend:55002/schedule/"
BASE_URL = "http://backend:55002"

logger = logging.getLogger('frontLogger')
logger.addHandler(logging.StreamHandler(sys.stdout))
logger.setLevel(logging.DEBUG)

def get_leaders() -> list[Any]:
    data = [group[2] for group in get_groups()]
    logger.debug(f'get_leaders: {data}')
    return data

def get_groups() -> list[Any]:
    response = requests.get(GROUPS_ADDRESS + "get/")
    response.raise_for_status()
    data = response.json()["data"]
    logger.debug(f'get_groups: {data}')
    return data

def get_students() -> list[Any]:
    response = requests.get(STUDENTS_ADDRESS + "get/")
    response.raise_for_status()
    data = response.json()["data"]
    logger.debug(f'get_students: {data}')
    return data

def get_student(student_id: int) -> Optional[Any]:
    response = requests.get(
        STUDENTS_ADDRESS + "getById/",
        json={"studentId": student_id},
        headers={'Content-Type': 'application/json'}
    )
    response.raise_for_status()
    data = response.json().get("data")
    logger.debug(f'get_student: {data}')
    return data

def get_schedule() -> list[Any]:
    response = requests.get(SCHEDULE_ADDRESS + "get/")
    response.raise_for_status()
    data = response.json()["data"]
    logger.debug(f'get_schedule: {data}')
    return data

def add_student(group: int, name: str, surname: str) -> int:
    url = f"{BASE_URL}/students/change/add/"
    data = {
        "groupId": group,
        "name": name,
        "surname": surname
    }
    response = requests.post(url, json=data)
    response.raise_for_status()
    return response.json().get("studentId")

def delete_student(student_id: int) -> None:
    response = requests.post(STUDENTS_ADDRESS + "change/delete/",
        json={"studentId": student_id},
        headers={'Content-Type': 'application/json'}
    )
    response.raise_for_status()

def change_group(student_id: int, new_group_id: int) -> None:
    response = requests.put(STUDENTS_ADDRESS + "change/group/",
        json={
            "studentId": student_id,
            "groupId": new_group_id
        },
        headers={'Content-Type': 'application/json'}
    )
    response.raise_for_status()

def upload_student_image(student_id: int, image_file: Any) -> None:
    url = f"{BASE_URL}/students/{student_id}/upload_image/"
    files = {'file': image_file}
    response = requests.post(url, files=files)
    response.raise_for_status()

def get_student_image(student_id: int) -> Optional[bytes]:
    url = f"{BASE_URL}/students/{student_id}/get_image/"
    response = requests.get(url)
    if response.status_code == 200:
        return response.content
    return None

def delete_student_image(student_id: int) -> None:
    url = f"{BASE_URL}/students/{student_id}/delete_image/"
    response = requests.delete(url)
    response.raise_for_status()


