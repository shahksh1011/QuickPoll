var express = require('express');
var router = express.Router();
const admin = require('../firebase-admin/admin');
const FirebaseAuth = require('firebaseauth'); // or import FirebaseAuth from 'firebaseauth';
const firebase = new FirebaseAuth(process.env.FIREBASE_API_KEY);

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
      res.send(200, { data: result });
    }
  });
});

router.post('/register', function (req, res) {

  firebase.registerWithEmail(email, password, name, function (err, result) {
    if (err){
      console.log(err);
    }
    else{
      console.log(result);
    }
  });
});

module.exports = router;
