//
//  lelaViewController.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/22/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "UIViewController+lela.h"
#import "UINavigationBar+lela.h"
#import "ApplicationModel.h"
#import "LelaTabbar.h"

@class UINavigationBar;

@interface lelaViewController : UIViewController <LelaTabbarDelegate>
{
    
    
    ApplicationModel *applicationModel;
    
    //UINavigationBar *header;
    LelaTabbar *tabBar;
	
    UILabel *dLabel;
    UILabel *fLabel;
}

@property (nonatomic, retain) ApplicationModel *applicationModel;

@property (nonatomic, retain) IBOutlet UINavigationBar *header;
@property (nonatomic, retain) LelaTabbar *tabBar;


@property (nonatomic, retain) UILabel *dLabel;
@property (nonatomic, retain) UILabel *fLabel;


-(void) addView;
-(void) showLoggedInTabbar;
-(IBAction) back:(id)sender;


- (void)setTitle:(NSString *)value;
-(void) echo;
-(void) dump;

@end

