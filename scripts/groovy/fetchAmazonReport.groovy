@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.2' )
@Grab(group='org.apache.httpcomponents', module='httpclient', version='4.2' )

//  https://affiliate-program.amazon.com
// /gp/associates/login/login.html
// https://affiliate-program.amazon.com/gp/flex/sign-in/select.html
//  https://affiliate-program.amazon.com/gp/associates/network/reports/report.html?ie=UTF8&reportType=earningsReport
//  https://affiliate-program.amazon.com
// /gp/associates/network/reports/report.html?tag=&reportType=earningsReport&program=all&periodType=preSelected&preSelectedPeriod=yesterday&startMonth=5&startDay=19&startYear=2012&endMonth=5&endDay=19&endYear=2012&submit.download_CSV.x=50&submit.download_CSV.y=12&submit.download_CSV=Download+report+%28CSV%29

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.URLENC
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.protocol.HttpContext
import org.apache.http.impl.client.DefaultRedirectStrategy

def http = new HTTPBuilder( 'https://affiliate-program.amazon.com' )
// auth omitted...
def postBody = [email:'michael.ball@lela.com',password:'uHwOm86RcYirj0wG'] // will be url-encoded

http.post( path: '/gp/associates/login/login.html', body: postBody,
        requestContentType: URLENC ) { resp ->

    println "Response status: ${resp.statusLine}"
    assert resp.statusLine.statusCode == 200
}

http.client.setRedirectStrategy(new DefaultRedirectStrategy() {
    @Override
    boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
        def redirected = super.isRedirected(request, response, context)
        return redirected || response.getStatusLine().getStatusCode() == 302
    }
})

http.get( path: '/gp/associates/network/reports/report.html?tag=&reportType=earningsReport&program=all&periodType=preSelected&preSelectedPeriod=yesterday&startMonth=5&startDay=19&startYear=2012&endMonth=5&endDay=19&endYear=2012&submit.download_CSV.x=50&submit.download_CSV.y=12&submit.download_CSV=Download+report+%28CSV%29', contentType:'text/plain')
        { resp, reader->
            println "Response status: ${resp.statusLine}"

            System.out << reader
            assert resp.statusLine.statusCode == 200
            println resp.dump()
            //println resp.context.delegate.map['http.response'].dump()
            //println resp.responseBase.dump()
            println resp.entity.dump()
            println resp.entity.wrappedEntity.dump()
            System.out << resp.entity.wrappedEntity.content
            System.out << resp.entity.wrappedEntity.wrappedEntity.content



        }

