/*
 * Copyright 2018-2019, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */

// Code generated by client-gen. DO NOT EDIT.

package fake

import (
	v1beta1 "github.com/enmasseproject/enmasse/pkg/apis/enmasse/v1beta1"
	v1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	labels "k8s.io/apimachinery/pkg/labels"
	schema "k8s.io/apimachinery/pkg/runtime/schema"
	types "k8s.io/apimachinery/pkg/types"
	watch "k8s.io/apimachinery/pkg/watch"
	testing "k8s.io/client-go/testing"
)

// FakeAddressSpaces implements AddressSpaceInterface
type FakeAddressSpaces struct {
	Fake *FakeEnmasseV1beta1
	ns   string
}

var addressspacesResource = schema.GroupVersionResource{Group: "enmasse.io", Version: "v1beta1", Resource: "addressspaces"}

var addressspacesKind = schema.GroupVersionKind{Group: "enmasse.io", Version: "v1beta1", Kind: "AddressSpace"}

// Get takes name of the addressSpace, and returns the corresponding addressSpace object, and an error if there is any.
func (c *FakeAddressSpaces) Get(name string, options v1.GetOptions) (result *v1beta1.AddressSpace, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewGetAction(addressspacesResource, c.ns, name), &v1beta1.AddressSpace{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AddressSpace), err
}

// List takes label and field selectors, and returns the list of AddressSpaces that match those selectors.
func (c *FakeAddressSpaces) List(opts v1.ListOptions) (result *v1beta1.AddressSpaceList, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewListAction(addressspacesResource, addressspacesKind, c.ns, opts), &v1beta1.AddressSpaceList{})

	if obj == nil {
		return nil, err
	}

	label, _, _ := testing.ExtractFromListOptions(opts)
	if label == nil {
		label = labels.Everything()
	}
	list := &v1beta1.AddressSpaceList{ListMeta: obj.(*v1beta1.AddressSpaceList).ListMeta}
	for _, item := range obj.(*v1beta1.AddressSpaceList).Items {
		if label.Matches(labels.Set(item.Labels)) {
			list.Items = append(list.Items, item)
		}
	}
	return list, err
}

// Watch returns a watch.Interface that watches the requested addressSpaces.
func (c *FakeAddressSpaces) Watch(opts v1.ListOptions) (watch.Interface, error) {
	return c.Fake.
		InvokesWatch(testing.NewWatchAction(addressspacesResource, c.ns, opts))

}

// Create takes the representation of a addressSpace and creates it.  Returns the server's representation of the addressSpace, and an error, if there is any.
func (c *FakeAddressSpaces) Create(addressSpace *v1beta1.AddressSpace) (result *v1beta1.AddressSpace, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewCreateAction(addressspacesResource, c.ns, addressSpace), &v1beta1.AddressSpace{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AddressSpace), err
}

// Update takes the representation of a addressSpace and updates it. Returns the server's representation of the addressSpace, and an error, if there is any.
func (c *FakeAddressSpaces) Update(addressSpace *v1beta1.AddressSpace) (result *v1beta1.AddressSpace, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateAction(addressspacesResource, c.ns, addressSpace), &v1beta1.AddressSpace{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AddressSpace), err
}

// UpdateStatus was generated because the type contains a Status member.
// Add a +genclient:noStatus comment above the type to avoid generating UpdateStatus().
func (c *FakeAddressSpaces) UpdateStatus(addressSpace *v1beta1.AddressSpace) (*v1beta1.AddressSpace, error) {
	obj, err := c.Fake.
		Invokes(testing.NewUpdateSubresourceAction(addressspacesResource, "status", c.ns, addressSpace), &v1beta1.AddressSpace{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AddressSpace), err
}

// Delete takes name of the addressSpace and deletes it. Returns an error if one occurs.
func (c *FakeAddressSpaces) Delete(name string, options *v1.DeleteOptions) error {
	_, err := c.Fake.
		Invokes(testing.NewDeleteAction(addressspacesResource, c.ns, name), &v1beta1.AddressSpace{})

	return err
}

// DeleteCollection deletes a collection of objects.
func (c *FakeAddressSpaces) DeleteCollection(options *v1.DeleteOptions, listOptions v1.ListOptions) error {
	action := testing.NewDeleteCollectionAction(addressspacesResource, c.ns, listOptions)

	_, err := c.Fake.Invokes(action, &v1beta1.AddressSpaceList{})
	return err
}

// Patch applies the patch and returns the patched addressSpace.
func (c *FakeAddressSpaces) Patch(name string, pt types.PatchType, data []byte, subresources ...string) (result *v1beta1.AddressSpace, err error) {
	obj, err := c.Fake.
		Invokes(testing.NewPatchSubresourceAction(addressspacesResource, c.ns, name, pt, data, subresources...), &v1beta1.AddressSpace{})

	if obj == nil {
		return nil, err
	}
	return obj.(*v1beta1.AddressSpace), err
}
