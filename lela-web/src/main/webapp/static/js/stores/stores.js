/*
 * Copyright (c) 2012. Lela.com.
 */

$().ready(function($) {

    // init the my faves events
    initList();
    var merchants = [{"rlnm":"gotobabycom","url":"http://r.popshops.com/r/NjIweGcvRGZmMUw3STVvekZZTkVFYUZHQ1hLaTdjN21RYlZPVDgwQUduND0K"},{"rlnm":"neweggcom","url":"http://r.popshops.com/r/SDlFMkhtMXhJalZtaG1qREpnUWE0TGV6MXJ0LzdzN25Ib1hjQUtYRENTTT0K"},{"rlnm":"pcmall","url":"http://r.popshops.com/r/OFBQbm9CNUJ1WnFKRFoxTWNJSHBGTUdJT3F3WWt0V3QxNlJiczFHdEZTdz0K"},{"rlnm":"dimplesanddandelions","url":"http://r.popshops.com/r/Z25UdWxrZHluaWdDNXRJU1k1MHBNZW8zMEpTODJyTGk2WjZnUFIzZVM4MD0K"},{"rlnm":"walmart","url":"http://r.popshops.com/r/NjIweGcvRGZmMUw3STVvekZZTkVFVC9lVjJrb1VSMXllenZKUjU4dEhOdz0K"},{"rlnm":"kohls","url":"http://r.popshops.com/r/YmhnUnNqTHA0ejlRNXFIdnhjMk9aVnNVRmNaSVRLL0I3bUZHWkhUekpXMD0K"},{"rlnm":"searscom","url":"http://r.popshops.com/r/aVFDaktkaTZPSkxxVHk4ejU0TmlnTGNGb0lkNFloMnJxY3RrR2V5TE9XTT0K"},{"rlnm":"target","url":"http://r.popshops.com/r/NjIweGcvRGZmMUw3STVvekZZTkVFWFhWdUhVdFBEU3p5eGpiOFFMNG9PZz0K"},{"rlnm":"drugstorecom","url":"http://r.popshops.com/r/RU02MXUzNVRONW5rTEF1MURmWXg5ZGhpUUZXcmJENW9xMXZGMGNjaFBtWT0K"},{"rlnm":"hphomeandhomeofficestore","url":"http://r.popshops.com/r/SDlFMkhtMXhJalZtaG1qREpnUWE0Q2lrRmpFZjRFT0FOeWRjS21QbFM4RT0K"},{"rlnm":"beautycom","url":"http://r.popshops.com/r/YktUNTdWQ3NRUkFrWlp3VHVmaUtEcDVtMkp6MktESGloWENlK2tuVm5aRT0K"},{"rlnm":"tigerdirect","url":"http://r.popshops.com/r/RzJuVTJVR3V1TDR5Y0w2dUtFVzF1dW9ERmVqVkZ4TDVCdlNiamxDT1E3OD0K"},{"rlnm":"bestbuy","url":"http://r.popshops.com/r/bC8rd1AvWGRvQUxwRTF6OVhzOEdSbHh3YTRGVjZqTXI1dGdHZEp5dVA4ST0K"},{"rlnm":"macys","url":"http://r.popshops.com/r/RExqQXhrazREYWNzZDlPczBncFMvWnkzNDJEZUhnUThhNDhxRE8rRGE0dz0K"},{"rlnm":"jrcom","url":"http://r.popshops.com/r/aVFDaktkaTZPSkxxVHk4ejU0TmlnRzlsaDZ6d1hDQ0EyUUFIdEVVd0pHYz0K"},{"rlnm":"homedepot","url":"http://r.popshops.com/r/SDlFMkhtMXhJalZtaG1qREpnUWE0S2V1Y20zMCtrMlJXMlJBdFpDWG1XUT0K"},{"rlnm":"mystrollerscom","url":"http://r.popshops.com/r/YmhnUnNqTHA0ejlRNXFIdnhjMk9aU1RoRDFUaVZkdVd4STgzR0FTbmVVaz0K"},{"rlnm":"diaperscom","url":"http://r.popshops.com/r/UGxlZEVCZWE3azlML0NDd01jR1ZFc0JSc2dmM3FndGpHTEVFMGI0b2FZcz0K"},{"rlnm":"giggle","url":"http://r.popshops.com/r/aVFDaktkaTZPSkxxVHk4ejU0TmlnSkI5cUY4cTE4WnU5T2xMbHR4SlhaST0K"},{"rlnm":"amazon","url":"http://r.popshops.com/r/b0JOSEdocG14NUs3eU93eWNiWktPV0dLeWtoREFkRS96Tkp3VTdQVGFRcz0K"},{"rlnm":"everythingfurniture","url":"http://r.popshops.com/r/Z25UdWxrZHluaWdDNXRJU1k1MHBNZmQvRGhSWm1xSmtJRDBicExuNU9CVT0K"},{"rlnm":"adorama","url":"http://www.jdoqocy.com/click-5391124-10784043"},{"rlnm":"magicbeans","url":"http://r.popshops.com/r/bGY0MjJCeWlmclJzT1FtTWtKdk5mT3NPRmFvcHNRejJJNnBYbEdySmVaaz0K"},{"rlnm":"buycom","url":"http://r.popshops.com/r/UGxlZEVCZWE3azlML0NDd01jR1ZFaW54empIOUxBMFVTOVRpVTBzSUJmaz0K"},{"rlnm":"babybeddingzonecom","url":"http://r.popshops.com/r/NjIweGcvRGZmMUw3STVvekZZTkVFZGJEaGJmellGSk44ZFpPdjI4QURDST0K"},{"rlnm":"barnesandnoblecom","url":"http://r.popshops.com/r/R2s2MFA5NW9UcHVYVkFwbk9Gc0tCYlZURkMwUXpINXk4VjRubm1TU0pyYz0K"},{"rlnm":"wagcom","url":"http://r.popshops.com/r/SDlFMkhtMXhJalZtaG1qREpnUWE0SDl1K2ZMQ0hsS0RDWU91SFRZTldkMD0K"},{"rlnm":"qvccom","url":"http://www.tkqlhce.com/click-5391124-9254861"},{"rlnm":"searsoutlet","url":"http://r.popshops.com/r/VmI2alpPaDA0a2xPYmZZMTRyNWpXUXVlcU5jZXRZMEhZVFlraktkLzNlND0K"},{"rlnm":"sonycomputersandelectronics","url":"http://r.popshops.com/r/alJUSUpNOWJNZFV4R2dwOVh4YnhuL3o0dDJwaG1McW1VaW1GTVBBK3o0bz0K"},{"rlnm":"macmall","url":"http://r.popshops.com/r/SXNXb05XTnZIYSt2V3R2WnV2eDB0U00xeVpkVUxGQWJKN2Znam5ieEZPaz0K"},{"rlnm":"pbskidsshop","url":"http://r.popshops.com/r/SXNXb05XTnZIYSt2V3R2WnV2eDB0VjI1cXRCT3dUM001VGRKM29VOGZpWT0K"},{"rlnm":"pcrichardandson","url":"http://www.jdoqocy.com/click-5391124-11075777"},{"rlnm":"yoyocom","url":"http://gan.doubleclick.net/gan_click?lid=41000613802464018&amp;pubid=21000000000504648&amp;lsrc=17"},{"rlnm":"mattelshop","url":"http://www.anrdoezrs.net/click-5391124-10981835"},{"rlnm":"canon","url":"http://r.popshops.com/r/aVFDaktkaTZPSkxxVHk4ejU0TmlnREZKWjYzK2UvQWI5aGdNZmgrd2VRdz0K"}];

    if($('.lela-page-container').hasClass('page-local-stores')) {
        // init the local store events
        initLocalStores();
    }

    for(m in merchants) {
        $('.logo-link[rlnm='+merchants[m].rlnm+']').attr('href', merchants[m].url);
    }

    $('.view-deals').fancybox({
        'hideOnContentClick': true,
        'width': 924,
        'height': 600,
        'type': 'iframe'
    });

});
