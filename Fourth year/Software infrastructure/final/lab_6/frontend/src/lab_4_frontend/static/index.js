import axios from "https://cdn.jsdelivr.net/npm/axios@1.3.5/+esm";

document
    .getElementById("groups")
    .addEventListener("change", filterElementsByGroup);
function filterElementsByGroup() {
    const group = document.getElementById("groups").value;
    const students = document
        .getElementById("content-list")
        .getElementsByTagName("tr");
    Array.from(students).forEach((element) => {
        if (element.classList.contains(group)) element.hidden = false;
        else element.hidden = true;
    });
}
filterElementsByGroup();

Array.from(document.getElementsByClassName("deleteStudent")).forEach(
    (element) => {
        element.addEventListener("click", async () => {
            try {
                await axios.post("/deleteStudent/commit", { id: element.id });
                location.reload();
            } catch (error) {
                console.error("Error deleting student:", error);
                alert("Помилка при видаленні студента.");
            }
        });
    }
);

let currentStudentId = null;
let uploadImageModal = null;

function openImageUploadModal(studentId) {
    currentStudentId = studentId;
    uploadImageModal = new bootstrap.Modal(
        document.getElementById("uploadImageModal")
    );
    uploadImageModal.show();
}

async function uploadImage() {
    const fileInput = document.getElementById("studentImageFile");
    const file = fileInput.files[0];

    if (!file) {
        alert("Будь ласка, виберіть файл");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
        const response = await axios.post(
            `/students/${currentStudentId}/upload_image/`,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            }
        );
        if (response.data.success) {
            alert("Фото успішно завантажено!");
            uploadImageModal.hide();
            location.reload();
        }
    } catch (error) {
        console.error("Error uploading image:", error);
        alert("Помилка при завантаженні фото.");
    }
}

async function deleteImage(studentId) {
    try {
        const response = await axios.delete(
            `/students/${studentId}/delete_image/`
        );
        if (response.data.success) {
            alert("Фото успішно видалено!");
            location.reload();
        }
    } catch (error) {
        console.error("Error deleting image:", error);
        alert("Помилка при видаленні фото.");
    }
}

document.querySelectorAll(".btn-close, .btn-secondary").forEach((button) => {
    button.addEventListener("click", () => {
        if (uploadImageModal) {
            uploadImageModal.hide();
        }
    });
});

window.openImageUploadModal = openImageUploadModal;
window.uploadImage = uploadImage;
window.deleteImage = deleteImage;
