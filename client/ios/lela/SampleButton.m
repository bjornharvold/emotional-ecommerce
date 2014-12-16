//
//  SampleButton.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/23/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "SampleButton.h"

@implementation SampleButton

@synthesize temp;

- (id)init
{
    self = [super init];
    if (self) {
        // Initialization code here.
    }
    
    return self;
}

- (void)dealloc 
{
    [_temp release];
    [super dealloc];
}

@end
