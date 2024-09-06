"use strict";

const fs = require('fs');

class Session {
    #sessions = {};

    constructor() {
        try {
            const sessionContents = fs.readFileSync('./sessions.json', 'utf8');
            this.#sessions = JSON.parse(sessionContents.trim());
        } catch (e) {
            this.#sessions = {};
        }
    }

    #storeSessions() {
        const sessionContents = JSON.stringify(this.#sessions);
        fs.writeFileSync('./sessions.json', sessionContents, 'utf-8');
    }

    setSession(key, value) {
        if (!value) {
            value = {};
        }

        this.#sessions[key] = value;
        this.#storeSessions();
    }

    getSession(key) {
        return this.#sessions[key];
    }

    finish(req) {
        const sessionId = req.sessionId;
        delete this.#sessions[sessionId];
        this.#storeSessions();
    }
}

module.exports = Session;
