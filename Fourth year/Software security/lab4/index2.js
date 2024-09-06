'use strict';

const express = require('express');
const bodyParser = require('body-parser');
const request = require('request');

const Session = require('./Session');
const { generateLoginRequestOptions } = require('./utils');

const app = express();
const port = 3000;

app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const sessions = new Session();

app.use((req, res, next) => {
    let currentSession = {};
    const sessionId = req.get('Authorization');

    if (sessionId) {
        currentSession = sessions.getSession(sessionId);
    }

    req.session = currentSession;
    req.sessionId = sessionId;
    next();
});

app.get('/page-info', (req, res) => {
    if (req.session.username) {
        return res.json({
            username: req.session.username,
            logout: 'http://localhost:3000/logout'
        });
    }

    res.json({
        error: 'Authentication was not successful. Try again'
    });
});

app.post('/api/login', (req, res) => {
    const { login, password } = req.body;
    const options = generateLoginRequestOptions(login, password);

    request(options, (error, response, body) => {
        if (error) {
            return res.status(401).send();
        }

        if (body) {
            const bodyjson = JSON.parse(body);

            if (bodyjson.error) {
                res.status(401).send();
            } else {
                sessions.setSession(bodyjson.access_token, { username: login });
                res.json({ token: bodyjson.access_token });
            }
        }
    });
});

app.get('/logout', (req, res) => {
    sessions.finish(req, res);
    res.redirect('/');
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`);
});
