const { Firestore } = require('@google-cloud/firestore');
var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const db = admin.firestore();
let docRef = db.collection('surveys');

/* GET users listing. */
router.get('/', function (req, res, next) {
    res.json({ users: [{ name: 'Timmy' }] });
});

router.post('/create', async function (req, res) {
    var survey = req.body.survey;
    let ref = docRef.doc();
    survey.id = ref.id
    survey.createdDate = Firestore.Timestamp.now();
    let date = new Date(survey.expiry);
    survey.expiry = Firestore.Timestamp.fromDate(date);
    let surveyDoc = await ref.set(survey);
    let userRef = db.collection('admin-users').doc(survey.createdBy);
    userRef.update({
        mySurveys: admin.firestore.FieldValue.arrayUnion(survey)
    });
    res.send(200, { message: 'Success' });
});

router.post('/response-data', async function (req, res) {
    var surveyId = req.body.surveyId;
    let ref = db.collection('surveyAnswers').doc(surveyId);
    let getDoc = ref.get()
        .then(doc => {
            if (!doc.exists) {
                console.log('No such document!');
            } else {
                console.log('Document data:', doc.data());
                var surveyData = doc.data();
                res.json(surveyData);
            }
        })
        .catch(err => {
            console.log('Error getting document', err);
        });
    res.send(200, { message: 'Success' });
});

module.exports = router;
