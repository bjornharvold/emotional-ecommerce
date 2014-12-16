//
//  GradientNavigationBar.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/23/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "GradientNavigationBar.h"

@implementation UINavigationBar (dropshadow)

//@dynamic setTitle;

-(void) willMoveToWindow:(UIWindow *)newWindow
{
    [super willMoveToWindow:newWindow];
    [self applyDefaultStyle];
}

- (IBAction)setTitle:(NSString *)value
{
	//self.topItem.title = @"Testing";
}


-(void) applyDefaultStyle 
{
    self.layer.shadowColor = [[UIColor blackColor] CGColor];
    self.layer.shadowOffset = CGSizeMake(0.0, 7.0);
    self.layer.shadowOpacity = 0.80;
}

@end