function changeImage() {
    const fileInput = document.getElementById("imageInput");
    const groupId = document.getElementById("groupId").value;
    const mimeType = fileInput.files[0].type;

    // Створення FormData об'єкта для передачі файлу
    const formData = new FormData();
    formData.append("image", fileInput.files[0]);
    formData.append("groupId", groupId);
    formData.append("mimetype", mimeType);

    // Відправка запиту на сервер
    fetch("http://host.docker.internal:5001/groups/change/image", {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);
        alert("Зображення змінено успішно!");
    })
    .catch(error => console.error("Error:", error));
}
