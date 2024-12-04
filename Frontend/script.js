const apiBaseUrl = "http://localhost:8080";

function appendToOutput(content) {
    const output = document.getElementById("output");
    output.innerHTML += `${content}\n`;
    output.scrollTop = output.scrollHeight;
}

async function input() {
    const inputField = document.getElementById("input");
    const userInput = inputField.value.trim();
    if (!userInput) return;

    appendToOutput(`> ${userInput}`);
    inputField.value = "";
    try {
        const response = await fetch(`${apiBaseUrl}/input`, {
            method: "POST",
            body: JSON.stringify({ userInput }),
            headers: {"Content-Type": "application/json",},
        });
        const result = await response.text();
        appendToOutput(result);
    } catch (error) {
        console.error("Error:", error);
        appendToOutput("Error communicating with server.");
    }
}

function handleKey(event) {
    if (event.key === "Enter") {
        input();
    }
}