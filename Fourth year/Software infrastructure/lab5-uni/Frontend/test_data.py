groups = [
    {
        "id": 1,
        "name":"group 1",
        "leaderId": 1,
        "img": "1.jpg"
    },
    {
        "id": 2,
        "name":"group 2",
        "leaderId": 2,
        "img": "2.jpg"
    }
]

students = [
    {
        "id": 1,
        "groupId": 1,
        "name": "name1aaaaaaa",
        "surname": "surname1"
    },
    {
        "id": 2,
        "groupId": 2,
        "name": "name2",
        "surname": "surname2"
    },
    {
        "id": 3,
        "groupId": 2,
        "name": "asdasdada",
        "surname": "ddddd"
    },
    {
        "id": 4,
        "groupId": 1,
        "name": "hgfjfhgjfg",
        "surname": "asda"
    },
    {
        "id": 5,
        "groupId": 1,
        "name": "ghfjdfjdf",
        "surname": "23"
    },
    {
        "id": 6,
        "groupId": 1,
        "name": "khkljlhj",
        "surname": "123123123"
    },
]

schedule = [
    {
        "groupId": 1,
        "day": 1,
        "pair": 1,
        "name": "IPZ",
        "description": "epic"
    },
    {
        "groupId": 1,
        "day": 1,
        "pair": 2,
        "name": "IPZ",
        "description": "more epic"
    },
    {
        "groupId": 2,
        "day": 2,
        "pair": 1,
        "name": "not IPZ",
        "description": "not epic"
    },
    {
        "groupId": 2,
        "day": 2,
        "pair": 2,
        "name": "English",
        "description": "bri'ish"
    },
    {
        "groupId": 2,
        "day": 2,
        "pair": 3,
        "name": "don't know",
        "description": "really"
    },
]

leaders = [group["leaderId"] for group in groups]