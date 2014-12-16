//
//  GradientButton.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/23/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "GradientButton.h"

@implementation UIButton (LelaButton)

//@dynamic __nextView;
//@dynamic __previousView;

- (id)init
{
    self = [super init];
    if (self) 
    {
        //self.__nextView = @"Step 2";
        //self.__previousView = @"Instructions";
        
        // Initialization code here.
    }
    
    return self;
}

-(void) dealloc
{
//    [nextView release];
  //  [previousView release];
    [super dealloc];
}

@end
