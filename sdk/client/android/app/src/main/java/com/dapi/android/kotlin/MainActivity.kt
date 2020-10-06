package com.dapi.android.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dapi.connect.core.base.DapiClient.Companion.getInstance
import com.dapi.connect.core.callbacks.OnDapiConnectListener
import com.dapi.connect.core.callbacks.OnDapiTransferListener
import com.dapi.connect.data.endpoint_models.*
import com.dapi.connect.data.models.DapiBeneficiaryInfo
import com.dapi.connect.data.models.DapiConnection
import com.dapi.connect.data.models.DapiError
import com.dapi.connect.data.models.LinesAddress
import java.util.*
import com.dapi.android.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dapiClient = getInstance()
        val connect = dapiClient.connect
        val autoFlow = dapiClient.autoFlow
        //Starting Connect
        val connectBtn = findViewById<Button>(R.id.connect)
        connectBtn.setOnClickListener { view: View? ->
            connect.present()
        }

        //Starting AutoFlow
        val autoFlowBtn = findViewById<Button>(R.id.autoFlow)
        autoFlowBtn.setOnClickListener { view: View? ->
            //You may pass accountID and amount to navigate directly to the numpad
            autoFlow.present()
        }

        //get cached connections
        connect.getConnections({

        }) {

        }

        //setting connect callbacks
        connect.setOnConnectListener(object : OnDapiConnectListener {
            //Called after successful connection.
            override fun onConnectionSuccessful(userID: String, bankID: String) {}

            //Called when the connection fails for any reason, you'll get info on the error in dapiError and logcat.
            override fun onConnectionFailure(dapiError: DapiError, bankID: String) {}

            //Called when the user taps continue (Shown after successful credentials input) on the webView then the webView disappears.
            //For example you may use this to show a loader while the connection is being processed in the SDK.
            override fun onProceed(userID: String, bankID: String) {}

            //You may use this to add a beneficiary to the newly connected account once the connection is done
            //Usually used when for example you're a seller and you want the newly connected account to add you as beneficiary Immediately
            //Note: Some banks has something called coolDownPeriod for adding a beneficiary that may take upto 2 days, so
            //by adding a beneficiary immediately here instead of adding the beneficiary when the buyer starts making a purchase,
            //you're actually winning some time.
            override fun setBeneficiaryInfoOnConnect(bankID: String): DapiBeneficiaryInfo? {
                return null
            }
        })

        //Setting autoflow callbacks
        autoFlow.setOnTransferListener(object : OnDapiTransferListener {
            //Called after a successful transfer
            override fun onAutoFlowSuccessful(
                    amount: Double,
                    senderAccountID: String?,
                    recipientAccountID: String?,
                    jobID: String) {

            }

            //Called when an error happens during making a transfer
            override fun onAutoFlowFailure(
                    error: DapiError, senderAccountID: String?, recipientAccountID: String?
            ) {

            }

            //The beneficiary to send the money to.
            override fun setBeneficiaryInfoOnAutoFlow(bankID: String): DapiBeneficiaryInfo {
                val linesAddress = LinesAddress()
                linesAddress.line1 = "line1"
                linesAddress.line2 = "line2"
                linesAddress.line3 = "line3"
                val accountNumber = "xxxx"
                val name = "xxxx"
                val bankName = "xxxx"
                val swiftCode = "xxxx"
                val iban = "xxxx"
                val country = "xxxx"
                val branchAddress = "xxxx"
                val branchName = "xxxx"
                val phoneNumber = "xxxx"
                return DapiBeneficiaryInfo(
                        linesAddress,
                        accountNumber,
                        name,
                        bankName,
                        swiftCode,
                        iban,
                        country,
                        branchAddress,
                        branchName,
                        phoneNumber
                )
            }

            //Optional
            override fun onPaymentStarted(bankID: String) {}
        })
        dapiClient.userID = "USER_ID" //This is Dapi's userID, you can get it using connect.getConnections() like above.
        //        dapiClient.setConfigurations() //Used when you want to update DapiConfigurations at runtime


        //**************Calling APIs************************

        //get owner identity info
        dapiClient.data.getIdentity(
                {

                }
        ) {

        }

        //Get accounts of the connection
        dapiClient.data.getAccounts(
                {

                }
        ) {

        }

        //Get balance of some connected account
        dapiClient.data.getBalance("accountID",
                {

                }
        ) {

        }

        //Get transactions that took place between fromDate and toDate
        val fromDate = Date()
        val toDate = Date()
        dapiClient.data.getTransactions("accountID", fromDate, toDate,
                {

                }
        ) {

        }

        //Get metadata of the connected account
        dapiClient.metadata.getAccountMetaData(
                {

                }
        ) {

        }

        //Get beneficiaries of the connected account
        dapiClient.payment.getBeneficiaries(
                {

                }
        ) {

        }

//        Will NOT compile, add params to DapiBeneficiaryInfo
//        Add beneficiary to the connected account
//        dapiClient.payment.createBeneficiary(DapiBeneficiaryInfo(),
//            {
//
//            }) {
//
//        }


        //Send money from one account to another
        dapiClient.payment.createTransfer(
                "receiverID", "senderID", 1.0, "remark",
                {

                }
        ) {

        }

        //Send money from one account to another
        dapiClient.payment.createTransfer(
                "iban", "name", "senderID", 1.0, "remark",
                {

                }
        ) {

        }

        //Delink an account.
        dapiClient.auth.delink(
                {

                }
        ) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val dapiClient = getInstance()
        dapiClient.release()
    }
}