//
//  UINavigationBar+lela.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/27/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "UINavigationBar+lela.h"

@implementation UINavigationBar (lela)

-(void) willMoveToWindow:(UIWindow *)newWindow
{
    [super willMoveToWindow:newWindow];
    [self applyStyle];
}



- (void)drawRect:(CGRect)rect 
{
    UIImage *image = [UIImage imageNamed: @"navigationBarGradientBackground.jpg"];
    [image drawInRect:CGRectMake(0, 0, self.frame.size.width, self.frame.size.height)];
}


-(void) applyStyle 
{
    self.layer.shadowColor = [[UIColor blackColor] CGColor];
    self.layer.shadowOffset = CGSizeMake(0.0, 7.0);
    self.layer.shadowOpacity = 0.80;
}

@end
