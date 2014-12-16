//
//  MotivationSelector.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/28/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MotivationSelector : UITableViewCell
{
    UIView *bg;
    UILabel *question;
	UILabel *motivatorA;
}

@property (nonatomic,retain) IBOutlet UILabel *question;
@property (nonatomic,retain) IBOutlet UILabel *motivatorA;
@property (nonatomic,retain) IBOutlet UIView *bg;

-(void) displayButtonRenderer;
-(IBAction) buttonHandler;

@end
