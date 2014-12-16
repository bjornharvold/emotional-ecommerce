//
//  SessionModel.h
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SessionModel : NSObject
{
    BOOL *hasCompletedQuiz;
    BOOL *showQuizOnStartup;
    NSDate *lastLogin;
    NSObject *lastScreen;
}

@property BOOL *showQuizOnStartup;
@property BOOL *hasCompletedQuiz;
@property (copy) NSDate *lastLogin;
@property (retain) NSObject *lastScreen;


/**
 * When called, exitSurvey will set the session specific
 * information such as: lastLogin, lastScreen, hasCompletedQuiz
 **/
-(IBAction) exitSurvey:(id)sender;

@end
