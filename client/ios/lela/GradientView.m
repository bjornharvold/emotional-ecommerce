//
//  GradientView.m
//  CoreLocationDemo
//
//  Created by Kenneth Lejnieks on 9/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "GradientView.h"


@implementation GradientView


- (id)initWithFrame:(CGRect)frame 
{
    
    self = [super initWithFrame:frame];
    if (self) 
	{
		//
    }
    return self;
}

- (void)drawRect:(CGRect)rect 
{
    // Drawing code.
    CGContextRef context = UIGraphicsGetCurrentContext();
	
    CGContextSaveGState(context);
    CGColorSpaceRef space = CGColorSpaceCreateDeviceRGB();
	
    CGFloat comps[] = {0.25, 0.25, 0.25, 0.90, 0, 0, 0, 1};
    CGGradientRef gradient = CGGradientCreateWithColorComponents(space, comps, NULL, 2);
	
    if (!CGContextIsPathEmpty(context))
    {
        CGContextClip(context);
    }
    
    CGContextDrawRadialGradient(context, gradient, self.center, 1.0, self.center, 320.0, kCGGradientDrawsBeforeStartLocation);
	
    CGContextRestoreGState(context);   
}

- (void)dealloc 
{
	[super dealloc];
}


@end
