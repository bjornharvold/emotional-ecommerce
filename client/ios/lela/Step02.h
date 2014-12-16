//
//  Step02.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/28/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "UIViewController+lela.h"

@interface Step02 : UIViewController
{
    UIButton *btn;
    UISegmentedControl *seg;
    
    NSDictionary *tableContents;
    NSArray *sortedKeys;
}


@property (nonatomic,retain) NSDictionary *tableContents;
@property (nonatomic,retain) NSArray *sortedKeys;

@property (nonatomic, retain) IBOutlet UIButton *btn;
@property (nonatomic, retain) IBOutlet UISegmentedControl *seg;



- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event;
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event;
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event;
- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event;

-(IBAction) onDragOver:(id)sender;
-(IBAction) onDragOut:(id)sender;

@end
