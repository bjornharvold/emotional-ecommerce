//
//  Step02.m
//  lela
//
//  Created by Kenneth Lejnieks on 9/28/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import "Step02.h"
#import "MotivationSelector.h"
#import "TableViewButtonCell.h"

//Navigation Tree
#import "Step03.h"


@implementation Step02

@synthesize btn;
@synthesize seg;
@synthesize tableContents;
@synthesize sortedKeys;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) 
    {
        //[self addTarget:self action:@selector(touchUpInside) forControlEvents:UIControlEventTouchUpInside];

    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    NSArray *arrTemp1 = [[NSArray alloc] initWithObjects:@"Andrew",nil];
	
    NSArray *arrTemp2 = [[NSArray alloc] initWithObjects:@"Bob",nil];
	
    NSArray *arrTemp3 = [[NSArray alloc] initWithObjects:@"Candice",nil];
    
	NSDictionary *temp =[[NSDictionary alloc] initWithObjectsAndKeys:arrTemp1,@"Motivator Question 1",
                         arrTemp2,@"Motivator Question 2",
                         arrTemp3,@"Motivator Question 3",
                         arrTemp3,@"Motivator Question 4",
                         nil];
    
	self.tableContents =temp;
	[temp release];
	self.sortedKeys =[[self.tableContents allKeys]
                      sortedArrayUsingSelector:@selector(compare:)];
	[arrTemp1 release];
	[arrTemp2 release];
	[arrTemp3 release];
    [super viewDidLoad];

    
    //btn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    //[btn setFrame: subview.bounds];
    //[seg addTarget:self action:@selector(onDragOver:) forControlEvents:UIControlEventTouchDragEnter];
    //[self.view addSubview:btn];
    
    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc 
{
    [tableContents release];
	[sortedKeys release];

    [super dealloc];
}

/*
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    NSLog(@"Method Call <touchesBegan>");
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *t = [touches anyObject];
    CGPoint touchLocation = [t locationInView:self.view];
    NSLog(@"%@", NSStringFromCGPoint(touchLocation));



//    NSLog(@"Method Call <touchesMoved> %@", touches);

}

- (IBAction)buttongDragged:(id)button withEvent:(UIEvent*)event {
    NSSet *touches = [event touchesForView:button];
    [self touchesMoved:touches withEvent:event];
}


- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    NSLog(@"Method Call <touchesEnded>");
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
    NSLog(@"Method Call <touchesCancelled>");
}
 */

///////////////////////////////////////////////////////////////////////////////////////////
-(void) addView
{
    Step03 *nextView = [[Step03 alloc] init];
	[self.view addSubview:nextView.view];
}

-(IBAction) nextView:(id)sender
{
    NSLog(@"Method Call");
    [self removeView];
    [self addView];
}

-(void)touchUpInside
{
    NSLog(@"touchUpInside Fired");
}


-(IBAction) onDragOver:(id)sender
{
    NSLog(@"onDragOver Fired");
}

-(IBAction) onDragOut:(id)sender
{
    NSLog(@"onDragOut Fired");
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [self.sortedKeys count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
	return [self.sortedKeys objectAtIndex:section];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section 
{
    NSString *sectionTitle = [self tableView:tableView titleForHeaderInSection:section];
    if (sectionTitle == nil) 
    {
        return nil;
    }
    
    // Create label with section title
    UILabel *label = [[[UILabel alloc] init] autorelease];
    label.frame = CGRectMake(20, 6, 300, 30);
    label.backgroundColor = [UIColor clearColor];
    label.textColor = [UIColor whiteColor];
    label.shadowColor = [UIColor blackColor];
    label.shadowOffset = CGSizeMake(0.0, 1.0);
    label.font = [UIFont boldSystemFontOfSize:16];
    label.text = sectionTitle;
    
    // Create header view and add label as a subview
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 50)];
    [view autorelease];
    [view addSubview:label];
    
    return view;
}

- (NSInteger)tableView:(UITableView *)table numberOfRowsInSection:(NSInteger)section 
{
	NSArray *listData =[self.tableContents objectForKey:[self.sortedKeys objectAtIndex:section]];
	return [listData count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath 
{
    NSLog(@"Index: %@", indexPath);
    
    //todo: refactor to only do this once
    tableView.backgroundColor = [UIColor clearColor];
    tableView.opaque = NO;
    tableView.backgroundView = nil;
    tableView.separatorColor = [UIColor clearColor];


    static NSString *CellIdentifier = @"MotivationSelector";
	
    MotivationSelector *cell = (MotivationSelector *) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) 
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"MotivationSelector" owner:self options:nil];
		
        for (id currentObject in topLevelObjects)
        {
            if ([currentObject isKindOfClass:[UITableViewCell class]])
            {
                cell =  (MotivationSelector *) currentObject;
                break;
            }
        }
    }
    
    cell.question.text = @"Sample Question";//[capitals objectAtIndex:indexPath.row];
    cell.motivatorA.text = @"Sample Answer";//[states objectAtIndex:indexPath.row];
    cell.backgroundColor = [UIColor clearColor];
    cell.opaque = NO;
    cell.backgroundView = nil;

    
    NSInteger sectionsAmount = [tableView numberOfSections];
    NSInteger rowsAmount = [tableView numberOfRowsInSection:[indexPath section]];
    if ([indexPath section] == sectionsAmount - 1 && [indexPath row] == rowsAmount - 1) 
    {
        NSLog(@"Last Row");
        [cell displayButtonRenderer];
    }
    	
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath 
{
    
	NSArray *listData =[self.tableContents objectForKey:[self.sortedKeys objectAtIndex:[indexPath section]]];
	NSUInteger row = [indexPath row];
//	NSString *rowValue = [listData objectAtIndex:row];
    
    [self nextView:[listData objectAtIndex:row]];
    
	
}

@end
