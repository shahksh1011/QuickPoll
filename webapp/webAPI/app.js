var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
const dotenv = require('dotenv');
dotenv.config();
const users = require('./routes/userRoutes');
const auth = require('./routes/authRoutes');
const surveys = require('./routes/surveyRoutes');
const polls = require('./routes/pollRoutes');
const admin = require('./firebase-admin/admin');
var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

app.use('/api/v1/user', users);
app.use('/auth',auth);
app.use('/api/v1/survey', surveys);
app.use('/api/v1/poll', polls)

module.exports = app;
