/*
 * Copyright 2018-2019, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

// Code generated by client-gen. DO NOT EDIT.

package fake

import (
	v1beta1 "github.com/enmasseproject/enmasse/pkg/apis/admin/v1beta1"
	v1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	labels "k8s.io/apimachinery/pkg/labels"
	schema "k8s.io/apimachinery/pkg/runtime/schema"
	types "k8s.io/apimachinery/pkg/types"
	watch "k8s.io/apimachinery/pkg/watch"
	testing "k8s.io/client-go/testing"
)

// FakeAuthenticationServices implements AuthenticationServiceInterface
type FakeAuthenticationServices struct {
	Fake *FakeAdminV1beta1
	ns   string
}

var authenticationservicesResource = schema.GroupVersionResource{Group: "admin.enmasse.io", Version: "v1beta1", Resource: "authenticationservices"}

var authenticationservicesKind = schema.GroupVersionKind{Group: "admin.enmasse.io", Version: "v1beta1", Kind: "AuthenticationService"}

// Get takes name of the authenticationService, and returns the corresponding authenticationService object, and an error if there is any.
func (c *FakeAuthenticationServices) Get(name string, options v1.GetOptions) (result *v1beta1.AuthenticationService, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewGetAction(authenticationservicesResource, c.ns, name), &v1beta1.AuthenticationService{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AuthenticationService), err
}

// List takes label and field selectors, and returns the list of AuthenticationServices that match those selectors.
func (c *FakeAuthenticationServices) List(opts v1.ListOptions) (result *v1beta1.AuthenticationServiceList, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewListAction(authenticationservicesResource, authenticationservicesKind, c.ns, opts), &v1beta1.AuthenticationServiceList{})

	if obj == nil {
		return nil, err
	}

	label, _, _ := testing.ExtractFromListOptions(opts)
	if label == nil {
		label = labels.Everything()
	}
	list := &v1beta1.AuthenticationServiceList{ListMeta: obj.(*v1beta1.AuthenticationServiceList).ListMeta}
	for _, item := range obj.(*v1beta1.AuthenticationServiceList).Items {
		if label.Matches(labels.Set(item.Labels)) {
			list.Items = append(list.Items, item)
		}
	}
	return list, err
}

// Watch returns a watch.Interface that watches the requested authenticationServices.
func (c *FakeAuthenticationServices) Watch(opts v1.ListOptions) (watch.Interface, error) {
	return c.Fake.
		InvokesWatch(testing.NewWatchAction(authenticationservicesResource, c.ns, opts))

}

// Create takes the representation of a authenticationService and creates it.  Returns the server's representation of the authenticationService, and an error, if there is any.
func (c *FakeAuthenticationServices) Create(authenticationService *v1beta1.AuthenticationService) (result *v1beta1.AuthenticationService, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewCreateAction(authenticationservicesResource, c.ns, authenticationService), &v1beta1.AuthenticationService{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AuthenticationService), err
}

// Update takes the representation of a authenticationService and updates it. Returns the server's representation of the authenticationService, and an error, if there is any.
func (c *FakeAuthenticationServices) Update(authenticationService *v1beta1.AuthenticationService) (result *v1beta1.AuthenticationService, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateAction(authenticationservicesResource, c.ns, authenticationService), &v1beta1.AuthenticationService{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AuthenticationService), err
}

// UpdateStatus was generated because the type contains a Status member.
// Add a +genclient:noStatus comment above the type to avoid generating UpdateStatus().
func (c *FakeAuthenticationServices) UpdateStatus(authenticationService *v1beta1.AuthenticationService) (*v1beta1.AuthenticationService, error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateSubresourceAction(authenticationservicesResource, "status", c.ns, authenticationService), &v1beta1.AuthenticationService{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AuthenticationService), err
}

// Delete takes name of the authenticationService and deletes it. Returns an error if one occurs.
func (c *FakeAuthenticationServices) Delete(name string, options *v1.DeleteOptions) error {
	_, err := c.Fake.
		Invokes(testing.NewDeleteAction(authenticationservicesResource, c.ns, name), &v1beta1.AuthenticationService{})

	return err
}

// DeleteCollection deletes a collection of objects.
func (c *FakeAuthenticationServices) DeleteCollection(options *v1.DeleteOptions, listOptions v1.ListOptions) error {
	action := testing.NewDeleteCollectionAction(authenticationservicesResource, c.ns, listOptions)

	_, err := c.Fake.Invokes(action, &v1beta1.AuthenticationServiceList{})
	return err
}

// Patch applies the patch and returns the patched authenticationService.
func (c *FakeAuthenticationServices) Patch(name string, pt types.PatchType, data []byte, subresources ...string) (result *v1beta1.AuthenticationService, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewPatchSubresourceAction(authenticationservicesResource, c.ns, name, pt, data, subresources...), &v1beta1.AuthenticationService{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AuthenticationService), err
}
