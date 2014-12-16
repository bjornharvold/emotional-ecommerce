//
//  DeviceModel.h
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DeviceModel : NSObject
{
    NSString *udid;
    BOOL *firstRun;
}

@property (copy) NSString *udid;
@property (assign) BOOL *firstRun;

-(NSString*) getMockUDID;

@end
