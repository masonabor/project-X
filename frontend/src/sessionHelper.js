export function setUserSession(userData, expiryTimeInDays) {
    const expiryTimestamp = Date.now() + expiryTimeInDays * 24 * 60 * 6000; // Конвертуємо хвилини у мілісекунди
    const sessionData = {
        user: userData,
        expiry: expiryTimestamp
    };
    sessionStorage.setItem("userSession", JSON.stringify(sessionData));
}

export function getUserSession() {
    const sessionData = sessionStorage.getItem("userSession");
    if (sessionData) {
        const parsedData = JSON.parse(sessionData);
        const currentTime = Date.now();

        // Перевіряємо, чи не минув час існування сесії
        if (currentTime < parsedData.expiry) {
            return parsedData.user;
        } else {
            // Якщо час минув, видаляємо сесію
            sessionStorage.removeItem("userSession");
            return null;
        }
    }
    return null;
}

export function clearUserSession() {
    sessionStorage.removeItem("userSession");
}

//import { setUserSession, getUserSession } from "./sessionHelper.js";