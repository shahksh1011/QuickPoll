const { Firestore } = require('@google-cloud/firestore');
var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const db = admin.firestore();
let docRef = db.collection('polls');

/* GET users listing. */
router.get('/', function (req, res, next) {
    res.json({ users: [{ name: 'Timmy' }] });
});

router.post('/create', async function (req, res) {
    var poll = req.body.poll;
    let ref = docRef.doc();
    poll.id = ref.id
    poll.createdDate = Firestore.Timestamp.now();
    let pollDoc = await ref.set(poll);
    let userRef = db.collection('admin-users').doc(poll.createdBy);
    userRef.update({
        myPolls: admin.firestore.FieldValue.arrayUnion(poll)
    });
    res.send(200, { message: 'Success' });
});

router.post('/get-data', async function (req, res) {
    let pollId = req.body.pollId;
    let pollRef = docRef.doc(pollId);
    let getDoc = pollRef.get()
        .then(doc => {
            if (!doc.exists) {
                console.log('No such document!');
            } else {
                console.log('Document data:', doc.data());
                var poll = doc.data();
                res.json(poll);
            }
        })
        .catch(err => {
            console.log('Error getting document', err);
        });
});

module.exports = router;
