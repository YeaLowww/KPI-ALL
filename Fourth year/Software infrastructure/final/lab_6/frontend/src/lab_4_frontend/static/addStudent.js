import axios from 'https://cdn.jsdelivr.net/npm/axios@1.3.5/+esm';

document.getElementById("addStudent").addEventListener("click", async () => {
    const group = document.getElementById("groups-big").value.replace("group-", "");
    const stName = document.getElementById("name").value.trim();
    const stSurname = document.getElementById("surname").value.trim();

    if (!stName || !stSurname) {
        alert("Заповніть усі поля!");
        return;
    }

    try {
        const response = await axios.post("/addStudent/commit/", {
            group: group,
            name: stName,
            surname: stSurname
        });

        if (response.data.success) {
            alert("Студента успішно додано!");
            window.location.href = "/";  // Перехід на головну сторінку після успішного додавання
        } else {
            console.error("Server response error:", response.data.error);
            alert("Помилка на сервері. Спробуйте пізніше.");
        }
    } catch (error) {
        if (error.response) {
            console.error("Server error:", error.response.data);
            alert(`Помилка: ${error.response.data.error || "невідома"}`);
        } else {
            console.error("Unknown error:", error);
            alert("Сталася невідома помилка. Перевірте підключення.");
        }
    }
});
