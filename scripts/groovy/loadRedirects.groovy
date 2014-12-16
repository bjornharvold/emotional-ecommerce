@GrabConfig(systemClassLoader = true)
@Grab(group = 'mysql', module = 'mysql-connector-java', version = '5.1.18')

import groovy.sql.Sql
import static groovy.io.FileType.*

Class.forName("com.mysql.jdbc.Driver")
def sql = Sql.newInstance("jdbc:mysql://localhost:3306/lela_eav_v2", "root",
        "password", "com.mysql.jdbc.Driver")

def redirects = new File("redirects.csv")

redirects.eachLine {
    def redirect = it.split('\t')
    sql.executeUpdate("update lela_eav_v2.affiliate_transaction set userId = ?, redirectUrl = ?, firstName=?, lastName=?, email =?, association =? where redirectId = ?", [redirect[1], redirect[6], redirect[3], redirect[4], redirect[2], redirect[5], redirect[0]])

}