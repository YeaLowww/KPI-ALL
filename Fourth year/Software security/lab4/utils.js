"use strict";

const generateLoginRequestOptions = (login, password) => ({
    method: "POST",
    url: "https://kpi.eu.auth0.com/oauth/token",
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    form: {
        client_id: 'JIvCO5c2IBHlAe2patn6l6q5H35qxti0',
        audience: 'https://kpi.eu.auth0.com/api/v2/',
        realm: 'Username-Password-Authentication',
        scope: 'offline_access',
        client_secret: 'ZRF8Op0tWM36p1_hxXTU-B0K_Gq_-eAVtlrQpY24CasYiDmcXBhNS6IJMNcz1EgB',
        username: login,
        password: password,
        grant_type: 'http://auth0.com/oauth/grant-type/password-realm',
    }
});

module.exports = {
    generateLoginRequestOptions
};
