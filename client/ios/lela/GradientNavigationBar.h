//
//  GradientNavigationBar.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/23/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>

@interface UINavigationBar (dropshadow) 

-(IBAction) setTitle:(NSString *)value;
-(void) applyDefaultStyle;

//@property (nonatomic, retain) IBOutlet UIView *setTitle;


@end