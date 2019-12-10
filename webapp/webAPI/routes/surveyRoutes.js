var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const db = admin.firestore();
let docRef = db.collection('surveys');

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.json({users: [{name: 'Timmy'}]});
});

router.post('/create', async function(req, res) {
    var survey = req.body.survey;
    let ref = docRef.doc();
    survey.id = ref.id
    let surveyDoc = await ref.set({ survey });
    res.send(200, {message: 'Success'});
  });

module.exports = router;
