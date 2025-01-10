import axios from 'https://cdn.jsdelivr.net/npm/axios@1.3.5/+esm';

document.getElementById("groups").addEventListener("change", filterElementsByGroup)
function filterElementsByGroup() {
    const group = document.getElementById("groups").value
    const students = document.getElementById("content-list").getElementsByTagName("tr")
    Array.from(students).forEach(element => {
        if (element.classList.contains(group)) element.hidden = false
        else element.hidden = true
    });
    
    const imgs = document.getElementsByTagName("img")
    Array.from(imgs).forEach(element => {
        if (element.classList.contains(group)) element.hidden = false
        else element.hidden = true
    });

    document.getElementById("imageGroupId").value = group.slice(6)
}
filterElementsByGroup()

Array.from(document.getElementsByClassName("deleteStudent")).forEach(element => {
    element.addEventListener("click", () => {
        axios.post("/deleteStudent/commit", {
            "id": element.id
        }).then(location.reload())
    })
})