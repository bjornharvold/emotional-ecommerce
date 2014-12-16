package com.lela.commons.comparator;

import org.springframework.social.facebook.api.Post;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Cruiser1
 * Date: 2/8/12
 * Time: 7:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookPostComparator  implements Comparator<Post> {
    /**
         * Method description
         *
         * @param a1 a1
         * @param a2 a2
         * @return Return value
         */
        @Override
        public int compare(Post a1, Post a2) {
            int result = 0;

            Date a1Date = null;
            Date a2Date = null;

            if (a1.getUpdatedTime() != null) {
                a1Date = a1.getUpdatedTime();
            } else {
                a1Date = a1.getCreatedTime();
            }

            if (a2.getUpdatedTime() != null) {
                a2Date = a2.getUpdatedTime();
            } else {
                a2Date = a2.getCreatedTime();
            }

            if ((a1Date != null) && (a2Date != null)) {
                if (a1Date.before(a2Date)) {
                    result = -1;
                } else if (a1Date.after(a2Date)) {
                    result = 1;
                }
            }

            return result;
        }
}
