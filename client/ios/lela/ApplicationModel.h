//
//  ApplicationModel.h
//  lela
//
//  Created by Kenneth Lejnieks on 9/29/11.
//  Copyright 2011 Lejnieks Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ProductsModel.h"
#import "ProductModel.h"
#import "DeviceModel.h"
#import "SessionModel.h"
#import "QuizModel.h"

@interface ApplicationModel : NSObject
{
    NSObject *config;
    DeviceModel *deviceModel;
    SessionModel *sessionModel;
    float rating;

    QuizModel *quizModel;
    
    ProductsModel *products;
    ProductModel *selectedProduct;
}



////////////////////////////////////////////////////////////
// CORE
////////////////////////////////////////////////////////////

/**
 * The application configuration informatiom
 **/
@property (copy) NSObject *config;

/**
 * Model data to store device specific information
 **/
@property (retain) DeviceModel *deviceModel;

/**
 * Model data to store session specific information
 **/
@property (retain) SessionModel *sessionModel;

/**
 * The application ratings informatiom
 **/
@property float rating;



////////////////////////////////////////////////////////////
// QUIZ
////////////////////////////////////////////////////////////

@property (retain) QuizModel *quizModel;



////////////////////////////////////////////////////////////
// PRODUCTS
////////////////////////////////////////////////////////////

/**
 * Model data to store the returned products
 * This will be used in 'Browse' and 'Search'
 **/
@property (copy) ProductsModel *products;

/**
 * Model data to store the selected product
 * This will be used in 'Scan' or after a product
 * has been selected from any tabular view
 **/
@property (assign) ProductModel *selectedProduct;



@end
