const request = require("request");
const dotenv = require("dotenv");

dotenv.config();

const options = {
  method: "PATCH",
    url: `${process.env.AUTH0_URL}/api/v2/users/auth0%7C5123121`,//auth0|5123121
  headers: {
    "content-type": "application/json",
    authorization: `Bearer ${process.env.AUTH0_TOKEN}`
  },
  form: {
    //client_id: process.env.AUTH0_CLIENT_ID,
    //client_secret: process.env.AUTH0_CLIENT_SECRET,
    //audience: `${process.env.AUTH0_URL}/api/v2/`,
    //grant_type: "client_credentials",
    //email: 'gapiyka@gmail.com',
    "connection": "Username-Password-Authentication",
    "password": "new!Password--1-12313123"
  }
};

request(options, function (error, response, body) {
  if (error) throw new Error(error);

  console.log(body);
});