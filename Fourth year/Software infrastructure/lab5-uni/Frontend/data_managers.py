import requests
from typing import Dict, List
import json 
GROUPS_ADRESS = "http://host.docker.internal:5001/groups/"
STUDENTS_ADRESS = "http://host.docker.internal:5001/students/"
SCHEDULE_ADRESS = "http://host.docker.internal:5001/schedule/"


""" GROUPS """

def get_groups() -> List[Dict]:
    return requests.post(GROUPS_ADRESS + "get/").json()

def get_leaders() -> List[Dict]:
    return [group["leaderId"] for group in get_groups()]

# blob image
def changeImage(groupId, mimetype, imgFile):
    return requests.post(GROUPS_ADRESS + "change/image/",
                         files={"image": imgFile},
                         data={"groupId": groupId, "mimetype": mimetype})


""" STUDENTS """

def get_students() -> List[Dict]:
    return requests.post(STUDENTS_ADRESS + "get/").json()

def get_student(id) -> List[Dict]:
    return requests.post(STUDENTS_ADRESS + "getById/",
        {"studentId": id}
    ).json()

def addStudent(groupId, name, surname):
    return requests.post(STUDENTS_ADRESS + "change/group",
        {
            "groupId": groupId,
            "name": name,
            "surname": surname
        }
    ).json()

def deleteStudent(studentId):
    return requests.post(STUDENTS_ADRESS + "change/group",
        {"studentId": studentId}
    ).json()

def changeGroup(studentId, newGroupId):
    return requests.post(STUDENTS_ADRESS + "change/group",
        {
            "studentId": studentId,
            "groupId": newGroupId
        }
    ).json()


""" SCHEDULE """

def get_schedule() -> List[Dict]:
    return requests.post(SCHEDULE_ADRESS + "get/").json()