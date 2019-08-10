import * as functions from 'firebase-functions';

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//const db = admin.firestore();

exports.myFunctions = functions.firestore
    .document('Customers/{cid}')
    .onCreate(async (snap, context) => {
        // var amount;
        // if(snap && snap.data()){
        //     amount = snap.data().amount;
        // }
            console.log('snap data ', snap.data());
            const userID = context.params.cid;
            console.log('customer id '+ userID);

            if (snap.data()){
                const data = snap.data();
                if(data != null){
                    const amount = data.amount;
                    const rate = data.rate;
                    const time = 0.0834;
                    const newValue = rate * amount * time;
                    const newDate = time;
                    console.log('new amount ',newValue);
                    const newData = {
                        intAmount: newValue,
                        nextDate: newDate
                    }
                    return admin.firestore().collection('Customers').doc(userID).update(newData);
                }
            }
    });
//firebase deploy --only functions
//C:\Users\Rahul Gaur\StudioProjects\MoneyManagement\firebaseFunction   