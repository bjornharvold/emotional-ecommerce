//
//  SessionModel.m
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "SessionModel.h"

@implementation SessionModel

@synthesize showQuizOnStartup;
@synthesize hasCompletedQuiz;
@synthesize lastLogin;
@synthesize lastScreen;


- (id)init
{ 
    if ((self = [super init]))
    {
        // Initialization code here.
    }
    
    return self;
}

-(IBAction) exitSurvey:(id)sender
{
    //
}

@end
