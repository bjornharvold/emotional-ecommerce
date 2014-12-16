//
//  MotivationSelector.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/28/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "MotivationSelector.h"

#import "Step02.h"

@implementation MotivationSelector


@synthesize bg;
@synthesize motivatorA;
@synthesize question;

UIView *buttonRenderer;
UIView *cellRenderer;

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {

        buttonRenderer = [self viewWithTag:1];
        buttonRenderer.hidden = YES;
        
        cellRenderer = [self viewWithTag:0];
        cellRenderer.hidden = NO;
        
        self.backgroundColor = [UIColor clearColor];
        self.backgroundView = nil;
        self.opaque = NO;

    }
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

-(IBAction) buttonHandler
{
    NSLog(@"Super View: %@", self.superview.superview);
    
    //Step02 *parent = (Step02 *) self.superview.superview;
    //[parent onDragOver];

}

-(void) displayButtonRenderer
{
    NSLog(@"displayButtonRenderer");

    cellRenderer.hidden = YES;
    buttonRenderer.hidden = YES;
}

@end
