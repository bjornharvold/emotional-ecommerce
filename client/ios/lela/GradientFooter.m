//
//  GradientFooter.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/23/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "GradientFooter.h"

@implementation UITabBar (dropshadow)

-(void) willMoveToWindow:(UIWindow *)newWindow
{
    [super willMoveToWindow:newWindow];
    [self applyDefaultStyle];
}

-(void) applyDefaultStyle
{
    self.layer.shadowColor = [[UIColor blackColor] CGColor];
    self.layer.shadowOffset = CGSizeMake(0.0, -7.0);
    self.layer.shadowOpacity = 0.8;
}

@end
