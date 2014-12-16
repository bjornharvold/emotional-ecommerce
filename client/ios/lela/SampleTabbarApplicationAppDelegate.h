//
//  SampleTabbarApplicationAppDelegate.h
//  SampleTabbarApplication
//
//  Created by Kenneth Lejnieks on 10/4/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SampleTabbarApplicationAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate>

@property (nonatomic, retain) IBOutlet UIWindow *window;

@property (nonatomic, retain) IBOutlet UITabBarController *tabBarController;

@end
