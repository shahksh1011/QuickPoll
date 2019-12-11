const {Firestore} = require('@google-cloud/firestore');
var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const db = admin.firestore();
let docRef = db.collection('polls');

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.json({users: [{name: 'Timmy'}]});
});

router.post('/create', async function(req, res) {
    var poll = req.body.poll;
    let ref = docRef.doc();
    poll.id = ref.id
    poll.createdDate = Firestore.Timestamp.now();
    let pollDoc = await ref.set(poll);
    res.send(200, {message: 'Success'});
  });

module.exports = router;
