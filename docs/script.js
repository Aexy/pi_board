//javascript for static changes
document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".toggle-btn");

    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const content = button.nextElementSibling;

            // Toggle display
            content.style.display === "block" ? content.style.display = "none" : content.style.display = "block";
        });
    });
});