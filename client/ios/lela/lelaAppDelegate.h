//
//  lelaAppDelegate.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/22/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>

@class lelaViewController;

@interface lelaAppDelegate : NSObject <UIApplicationDelegate>
{
    UIWindow *window;
	//UINavigationController *navigationController;

}

#define applicationDelegate \ ((lelaAppDelegate *)[UIApplication sharedApplication].delegate)

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet lelaViewController *viewController;
//@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

//@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;

@end
