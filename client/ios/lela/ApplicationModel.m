//
//  ApplicationModel.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/29/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "ApplicationModel.h"

@implementation ApplicationModel


@synthesize config;
@synthesize deviceModel;
@synthesize sessionModel;
@synthesize rating;

@synthesize quizModel;

@synthesize products;
@synthesize selectedProduct;



-(id) init 
{
    NSLog(@"######################################");
    
    if (self = [super init])
    {
        NSLog(@"######################################");
        NSLog(@"Initialize Application Model");
        NSLog(@"######################################");
        
        deviceModel = [[DeviceModel alloc] init];
        sessionModel = [[SessionModel alloc] init];
        
        //rating = _rating;
    }
    return self;
}

- (void)dealloc 
{
    [deviceModel dealloc];
    [sessionModel dealloc];
    
    [super dealloc];
}


@end
