//
//  ProductModel.h
//  lela
//
//  Created by Kenneth Lejnieks on 10/11/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Rating.h"


@interface ProductModel : NSObject
{
    int *lpid;
    NSString *title;
    NSString *subtitle;
    NSString *image;
    NSArray *images;
    NSArray *prices;
    int *local;
    int *online;
    NSArray *colors;
    NSString *shortDescription;
    NSString *description;
    NSString *features;
    NSString *specifications;
    BOOL *hasAccessories;
    NSArray *vendors;
    BOOL *hasWebsite;
    BOOL *onlinePurchase;
    Rating *ratings;
}

@property (assign) int *lpid;    //lela product id, this is the internal lela unique product id
@property (copy) NSString *title;
@property (copy) NSString *subtitle;
@property (retain) NSString *image;
@property (retain) NSArray *images;
@property (retain) NSArray *prices;
@property int *local;
@property int *online;
@property (retain) NSArray *colors;
@property (copy) NSString *shortDescription;
@property (copy) NSString *description;
@property (copy) NSString *features;
@property (copy) NSString *specifications;
@property BOOL *hasAccessories;
@property (retain) NSArray *vendors;
@property BOOL *hasWebsite;
@property BOOL *onlinePurchase;
@property (assign) Rating *ratings;


@end



