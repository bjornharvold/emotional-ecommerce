@GrabConfig(systemClassLoader = true)
@Grab(group = 'mysql', module = 'mysql-connector-java', version = '5.1.18')

import groovy.sql.Sql
import java.text.SimpleDateFormat;
import static groovy.io.FileType.*

Class.forName("com.mysql.jdbc.Driver")
def sql = Sql.newInstance("jdbc:mysql://localhost:3306/lela_eav_v2", "root",
        "password", "com.mysql.jdbc.Driver")

def arFolder = new File("/Users/ballmw/lela/trunk/temp")
//new File(System.properties.get("java.io.tmpdir") + "/affilatereporting")

def dateFormat1 = new SimpleDateFormat("M-d-yyyy H:mm:ss")
def dateFormat2 = new SimpleDateFormat("M-d-yy H:mm")
def dateFormat3 = new SimpleDateFormat("M/d/yyyy H:mm:ss")
def dateFormat4 = new SimpleDateFormat("MM/dd/yyyy H:mm:ss")
arFolder.eachFileMatch FILES, ~/.*\.txt/, {
    println it.name
    def existing_row = sql.firstRow("select * from lela_eav_v2.affiliate_report where FileName = ${it.name}")
    if (existing_row == null) {
        def details = sql.executeInsert("insert into lela_eav_v2.affiliate_report (DateCreated, DateModified, version, FileName, ReceivedDate ) values (null, null, 0, ${it.name}, ${new java.util.Date()})")
        println(details[0][0])
        def id = details[0][0]
        def format = "NEW"
        it.eachLine { line, line_number = 0 ->
            line_number++;
            if (line_number == 2 && line.startsWith("RevenueId")) {
                format = "OLD"
            }
            if (line_number > 2 && format == "NEW") {
                //Network	OrderId	Transaction Date	Referral Date	Referral Time	Transaction Time	Process Date	Advertiser Name	Sale Amount	Commission Amount	Product Name	Product Category	SKU/ Product ID	Quantity	SubID (Variable)	Tracking ID	Referring Product ID	Customer ID	UPC
                //Linkshare	2677149538031	6-4-2012	06-04-12	16:21	17:42:00	6/5/2012 2:14:00 PM	Wal-Mart.com USA, LLC	199.0000	7.9600	Child of Mine by Carter's My Nursery 3-in-1 Convertible Crib and Changing Table Combo, Mocha	Baby:Furniture:	W0001291415240	1	0604121621-4f21dd03279623000b007a3c-4fcce06dfc5652750e770aa4	0604121621	4f21dd03279623000b007a3c	4fcce06dfc5652750e770aa4
                def transaction = line.split('\t')
                //6-4-2012 17:42:00
                //M-d-yyyy H:mm:ss

                def transaction_date_s = transaction[2] + " " + transaction[5]
                def transaction_date = dateFormat1.parse(transaction_date_s)
                //06-04-12 16:21
                //M-d-yyyy H:mm
                def referral_date_s = transaction[3] + " " + transaction[4]
                def referral_date = new Date()
                if(referral_date_s != ' ' )
                  referral_date = dateFormat2.parse(referral_date_s)

                def process_date = dateFormat3.parse(transaction[6])
                def transaction_row = sql.executeInsert("""insert into lela_eav_v2.affiliate_transaction (DateCreated, DateModified, version, affiliateReportId, network, orderId, transactionDate, referralDate, advertiserName, salesAmount, commissionAmount, processDate, productName, productCategory, productId, quantity, subId, trackingId, referringProductId, redirectId )
                                       values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )""",
                        [null, null, 0, id, transaction[0], transaction[1], transaction_date, referral_date, transaction[7], transaction[8], transaction[9], process_date, transaction[10], transaction[11], transaction[12], transaction[13], transaction.length>14?transaction[14]:'', transaction.length>15?transaction[15]:'', transaction.length>16?transaction[16]:'', transaction.length>17?transaction[17]:'']
                )
            }
            else if (line_number > 2 && format == "OLD") {
                //RevenueId	ProviderId	Provider	OrganizationId	OrganizationName	EventDate	ClickDate	ProcessDate	ShipDate	MerchantId	MerchantName	WebsiteId	WebsiteName	SalesAmt	CommissionAmt	CurrencyType	CommissionType	Quantity	Status	Description	CustomerId	OrderId	KeywordId	SKU	LinkId	ActionId	ActionName	ProviderUid	AdID	CPID
                //18584858	1005	Linkshare	1962	Lela.com	06/01/2012 08:47:00 PM		06/03/2012 09:11:00 PM		2149	Wal-Mart.com USA, LLC			0.0000	0.0000			1				2677019418486	0531120158-4f3ee7a1ff30fd10d0007947-4fc6d05bfc56e05152ebba12	new_cust	0
                def transaction = line.split('\t')
                //6-4-2012 17:42:00
                //M-d-yyyy H:mm:ss

                def transaction_date_s = transaction[5]
                def transaction_date = dateFormat4.parse(transaction_date_s)
                //06-04-12 16:21
                //M-d-yyyy H:mm
                def referral_date_s = transaction[5]
                def referral_date = dateFormat4.parse(referral_date_s)

                def process_date = dateFormat4.parse(transaction[7])
                def transaction_row = sql.executeInsert("""insert into lela_eav_v2.affiliate_transaction (DateCreated, DateModified, version, affiliateReportId, network, orderId, transactionDate, referralDate, advertiserName, salesAmount, commissionAmount, processDate, productName, productCategory, quantity, subId, trackingId, referringProductId, redirectId )
                                       values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )""",
                        [null, null, 0, id, transaction[2], transaction[0], transaction_date, referral_date, transaction[10], transaction[13], transaction[14], process_date, "", "", transaction[17], null, null, transaction[22].contains('-')?transaction[22].split('-')[1]:null, transaction[22].contains('-')?transaction[22].split('-')[2]:null]
                )
            }
        }
    }
}