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
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 5 },
                    { type: "Foe", value: 10 },
                    { type: "Foe", value: 10 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 15 },
                    { type: "Foe", value: 20 },
                    { type: "Foe", value: 20 },
                    { type: "Weapon", value: 5, label: "Dagger" },
                    { type: "Weapon", value: 5, label: "Dagger" },
                    { type: "Weapon", value: 5, label: "Dagger" },
                    { type: "Weapon", value: 5, label: "Dagger" },
                ],
                [
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 30 },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 20, label: "Lance" },
                    { type: "Weapon", value: 20, label: "Lance" },
                    { type: "Weapon", value: 30, label: "Excalibur" },
                ],
                [
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 30 },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 20, label: "Lance" },
                    { type: "Weapon", value: 20, label: "Lance" },
                    { type: "Weapon", value: 30, label: "Excalibur" },
                ],
                [
                    { type: "Foe", value: 25 },
                    { type: "Foe", value: 30 },
                    { type: "Foe", value: 70 },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Horse" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 10, label: "Sword" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 15, label: "Battle-Axe" },
                    { type: "Weapon", value: 20, label: "Lance" },
                    { type: "Weapon", value: 20, label: "Lance" },
                ],
            ],
        };

        const response = await axios.post('http://127.0.0.1:8080/rig', rigData);
        console.log('Rigging Player Hands Response:', response.data);

        const eventsToRig = [
            { type: "Event", value: 1 },
            { type: "Event", value: 3 },
            { type: "Event", value: 2 },
        ];
        for (const event of eventsToRig) {
            await rigEventCard(event);
        }

        await driver.get('http://127.0.0.1:8081');

        await driver.wait(until.elementLocated(By.id('input')), 5000);
        let inputField = await driver.findElement(By.id('input'));
        let sendButton = await driver.findElement(By.id('send'));

        const actions = [
            "start",
            "y",
            "start",
            "1", "q",
            "2", "q",
            "3", "q",
            "4", "q",
            "start",
            "y", "y", "y",
            "start",
            "q",
            "q",
            "q",
            "q", "q", "q",
            "start",
            "q",
            "q",
            "q",
            "q", "q", "q",
            "start",
            "q",
            "q",
            "q",
            "q", "q", "q",
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
    } catch (error) {
        console.error("Test encountered an error:", error);
    } finally {
        await driver.quit();
    }
}

runTest();
