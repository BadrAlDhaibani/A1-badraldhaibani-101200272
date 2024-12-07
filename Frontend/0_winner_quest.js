const axios = require('axios');
const { Builder, By, until } = require('selenium-webdriver');

async function rigEventCard(eventCardData) {
    const response = await axios.post('http://127.0.0.1:8080/rig-event', eventCardData);
    console.log('Rig Event Response:', response.data);
}

async function runTest() {
    let driver = await new Builder().forBrowser('chrome').build();
    try {
        const rigData = {
            playerHands: [
                [
                    { type: "Foe", value: 50 },
                    { type: "Foe", value: 70 },
                    { type: "Weapon", value: 5, label: "Dagger" },
                    { type: "Weapon", value: 5, label: "Dagger" },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 20, label: "Lance" },
                    { type: "Weapon", value: 20, label: "Lance" },
                ],
                [
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 10 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 30 },
                    { type: "Foe", value: 30 },
                    { type: "Foe", value: 40 },
                    { type: "Weapon", value: 30, label: "Excalibur" },
                ],
                [
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 10 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 30 },
                    { type: "Foe", value: 40 },
                    { type: "Weapon", value: 20, label: "Lance" },
                ],
                [
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 10 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 30 },
                    { type: "Foe", value: 50 },
                    { type: "Weapon", value: 30, label: "Excalibur" },
                ],
            ],
        };

        const response = await axios.post('http://127.0.0.1:8080/rig', rigData);
        console.log('Rigging Player Hands Response:', response.data);

        const eventCardData = {
            type: "Event",
            value: 3,
        };
        await rigEventCard(eventCardData);

        await driver.get('http://127.0.0.1:8081');

        await driver.wait(until.elementLocated(By.id('input')), 5000);
        let inputField = await driver.findElement(By.id('input'));
        let sendButton = await driver.findElement(By.id('send'));

        const actions = [
            "start",
            "y",
            "start",
            "1", "2", "3", "4", "5", "6", "q",
            "start",
            "7", "8", "9", "10", "11", "12", "q",
            "start",
            "y", "y", "y",
            "start",
            "q",
            "q",
            "q",
            "q", "q", "q",
            "start",
        ];

        for (let action of actions) {
            await inputField.sendKeys(action);
            await sendButton.click();
            await driver.sleep(1000);
        }

        let output = await driver.findElement(By.id('output')).getText();
        console.log('Final Output:', output);

        if (!output.includes("Quest ends with no winners")) {
            throw new Error("Test failed: Quest did not end correctly.");
        }
        if (!output.includes("P1 draws 14 cards")) {
            throw new Error("Test failed: Incorrect card drawing for P1.");
        }
    } catch (error) {
        console.error("Test encountered an error:", error);
    } finally {
        await driver.quit();
    }
}

runTest();
