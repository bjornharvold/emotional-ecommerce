//
//  DeviceModel.m
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "DeviceModel.h"

@implementation DeviceModel

@synthesize udid;
@synthesize firstRun;


-(id) initWithParameters:(NSString*)_udid
{
    if ((self = [super init]))
    {
        udid = [_udid copy];
    }
    
    return self;
}

-(void) dealloc
{
    [udid release];
    udid = nil; 
    
    [super dealloc];
}

-(NSString*) getMockUDID
{
    return @"1234567890";
}

@end
