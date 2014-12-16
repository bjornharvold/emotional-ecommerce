//
//  LelaTabbarDelegate.h
//  lela
//
//  Created by Kenneth Lejnieks on 10/7/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LelaTabbar.h"

@class LelaTabbar;

@protocol LelaTabbarDelegate <NSObject>
    
-(void) lelaTabbar:(LelaTabbar *)tabBar didScrollToTabBarWithTag:(int)tag;
-(void) lelaTabbar:(LelaTabbar *)tabBar didSelectItemWithTag:(int)tag;

@end