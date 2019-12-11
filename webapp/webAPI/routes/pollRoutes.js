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

router.post('/response-data', async function (req, res) {
    var pollId = req.body.pollId;
    let ref = db.collection('pollAnswers').doc(pollId);
    let getDoc = ref.get()
        .then(doc => {
            if (!doc.exists) {
                console.log('No such document!');
            } else {
                console.log('Document data:', doc.data());
                var pollData = doc.data();
                res.json(pollData);
            }
        })
        .catch(err => {
            console.log('Error getting document', err);
        });
});

/*router.post('/poll-update-result', async function (req, res) {
    let pollId = req.body.pollId;
    const increment = admin.firestore.FieldValue.increment(1);
    let pollRef = db.collection('pollAnswers').doc(pollId);
    var responseData = [];
    let getDoc = pollRef.get()
        .then(doc => {
            if (!doc.exists) {
                console.log('No such document!');
            } else {
                console.log('Document data:', doc.data());
                var data = doc.data();
                const keys = Object.keys(data);
                var pollResponsesArray = [];
                for (const prop of keys) {
                    pollResponsesArray.push(data[prop]);
                }
                let pRef = docRef.doc(pollId);
                let docs = pRef.get()
                .then(result=>{
                    console.log(result.data());
                    var pollData = result.data();
                    responseData = [];
                    pollData.questions.forEach(element => {
                        var question = {
                            name: element.name,
                            options: []
                        }
                        element.options.forEach(str=>{
                            question.options.push({
                                str: str,
                                val: 0
                            })
                        })
                        responseData.push(question);
                    });
                    for(let i = 0; i<pollResponsesArray.length; i++){
                        var ele = pollResponsesArray[i];
                        const k = Object.keys(ele);
                        for(let j=0;j<k.length;j++){
                            for(let q=0;q<responseData[j].options.length; q++){
                                if(ele[j]===responseData[j].options[q].str)
                                    responseData[j].options[q].val += 1; 
                            }
                        }
                    }
                    res.json(responseData);
                }).catch(err => {
                    console.log('Error getting document', err);
                });
            }
        })
        .catch(err => {
            console.log('Error getting document', err);
        });
});*/

module.exports = router;
