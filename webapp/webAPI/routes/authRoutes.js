var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const FirebaseAuth = require('firebaseauth'); // or import FirebaseAuth from 'firebaseauth';
const firebase = new FirebaseAuth(process.env.FIREBASE_API_KEY);
const db = admin.firestore();
let docRef = db.collection('admin-users');
/* GET users listing. */
router.post('/login', function (req, res) {
  var email = req.body.email;
  var password = req.body.password;
  // var email = "admin@gmail.com";
  // var password = "password";
  firebase.signInWithEmail(email, password, function (err, result) {
    if (err) {
      console.log(err);
      res.send(200, { data: err })
    }
    else {
      console.log(result);
      let docLink = docRef.doc(result.user.id);
      let getDoc = docLink.get()
        .then(doc => {
          if (!doc.exists) {
            console.log('No such document!');
          } else {
            console.log('Document data:', doc.data());
            var user = doc.data().user;
            user.token = result.token;
            res.send(200, { user: user });
          }
        })
        .catch(err => {
          console.log('Error getting document', err);
        });
    }
  });
});

router.post('/register', function (req, res) {
  var user = req.body.user;
  console.log(user);
  firebase.registerWithEmail(user.email, user.password, user.firstName + ' ' + user.lastName, async function (err, result) {
    if (err) {
      console.log(err);
    }
    else {
      console.log(result);
      user.id = result.user.id;
      user.password = null;
      user.displayName = result.user.displayName;
      user.type = 'presenter';
      let doc = docRef.doc(user.id);
      let userDoc = await doc.set({ user });
      console.log(userDoc);
      user.token = result.token;
      res.send(200, { user: user });
    }
  });
});

module.exports = router;
