const { Firestore } = require('@google-cloud/firestore');
var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const db = admin.firestore();

/* GET users listing. */
router.get('/', function (req, res, next) {
  res.json({ users: [{ name: 'Timmy' }] });
});

router.post('/profile', function (req, res) {
  let userId = req.body.userId;
  let userRef = db.collection('admin-users').doc(userId);
  let getDoc = userRef.get()
    .then(doc => {
      if (!doc.exists) {
        console.log('No such document!');
      } else {
        console.log('Document data:', doc.data());
        var user = doc.data();
        res.json(user);
      }
    })
    .catch(err => {
      console.log('Error getting document', err);
    });
});

module.exports = router;
